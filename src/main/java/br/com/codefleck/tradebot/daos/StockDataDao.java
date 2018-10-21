package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.core.util.PaginatedList;
import br.com.codefleck.tradebot.models.StockData;

/**
 * @author Daniel Fleck
 */
@Repository
public class StockDataDao {


    @PersistenceContext
    private EntityManager manager;

    public List<StockData> all()
    {
        return manager.createQuery("select sd from StockData sd", StockData.class).getResultList();
    }

    public List<StockData> ListLatest(int max) {
        String query = "select sd from StockData sd ORDER BY id DESC";
        TypedQuery<StockData> results = manager.createQuery(query, StockData.class).setMaxResults(max);
        return results.getResultList();
    }

    public void save(StockData stockData){manager.persist(stockData);}

    public StockData findById(Integer id)
    {
        return manager.find(StockData.class, id);
    }

    public void remove(StockData stockData)
    {
        manager.remove(stockData);
    }

    public void update(StockData stockData)
    {
        manager.merge(stockData);
    }

    public PaginatedList paginated(int page, int max)
    {
        return new PaginatorQueryHelper().list(manager, StockData.class, page, max);
    }

}

