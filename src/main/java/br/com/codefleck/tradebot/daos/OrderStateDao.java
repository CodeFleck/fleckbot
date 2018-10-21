package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.core.util.PaginatedList;
import br.com.codefleck.tradebot.models.exchange.OrderState;

/**
 * @author Daniel Fleck
 */
@Repository
public class OrderStateDao {


    @PersistenceContext
    private EntityManager manager;

    public List<OrderState> all()
    {
        return manager.createQuery("select os from OrderState os", OrderState.class).getResultList();
    }

    public void save(OrderState orderState)
    {
        manager.persist(orderState);
    }

    public OrderState findById(Integer id)
    {
        return manager.find(OrderState.class, id);
    }

    public void remove(OrderState orderState)
    {
        manager.remove(orderState);
    }

    public void update(OrderState orderState)
    {
        manager.merge(orderState);
    }

    public PaginatedList paginated(int page, int max)
    {
        return new PaginatorQueryHelper().list(manager, OrderState.class, page, max);
    }

}



