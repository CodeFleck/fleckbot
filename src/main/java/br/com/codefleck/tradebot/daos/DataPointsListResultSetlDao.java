package br.com.codefleck.tradebot.daos;

import br.com.codefleck.tradebot.core.util.PaginatedList;
import br.com.codefleck.tradebot.models.DataPointsListResultSet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DataPointsListResultSetlDao {

    @PersistenceContext
    private EntityManager manager;

    public List<DataPointsListResultSet> all()
    {
        return manager.createQuery("select rs from DataPointsListResultSet rs", DataPointsListResultSet.class).getResultList();
    }

    public void save(DataPointsListResultSet dataPointsListResultSet)
    {
        manager.persist(dataPointsListResultSet);
    }

    public DataPointsListResultSet findById(Integer id)
    {
        return manager.find(DataPointsListResultSet.class, id);
    }

    public void remove(DataPointsListResultSet dataPointsListResultSet)
    {
        manager.remove(dataPointsListResultSet);
    }

    public void update(DataPointsListResultSet dataPointsListResultSet)
    {
        manager.merge(dataPointsListResultSet);
    }

    public PaginatedList paginated(int page, int max)
    {
        return new PaginatorQueryHelper().list(manager, DataPointsListResultSet.class, page, max);
    }

}

