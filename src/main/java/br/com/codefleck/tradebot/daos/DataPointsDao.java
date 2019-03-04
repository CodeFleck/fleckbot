package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.codefleck.tradebot.models.DataPoints;
import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.core.util.PaginatedList;

@Repository
public class DataPointsDao {

    @PersistenceContext
    private EntityManager manager;

    public List<DataPoints> all()
    {
        return manager.createQuery("select dp from DataPoints dp", DataPoints.class).getResultList();
    }

    public void save(DataPoints dataPoints)
    {
        manager.persist(dataPoints);
    }

    public DataPoints findById(Integer id)
    {
        return manager.find(DataPoints.class, id);
    }

    public void remove(DataPoints dataPoints)
    {
        manager.remove(dataPoints);
    }

    public void update(DataPoints dataPoints)
    {
        manager.merge(dataPoints);
    }

    public PaginatedList paginated(int page, int max)
    {
        return new PaginatorQueryHelper().list(manager, DataPoints.class, page, max);
    }

    public List<DataPoints> ListLatest(int max) {
        String query = "select dp from DataPoints dp ORDER BY dataPointsid DESC";
        TypedQuery<DataPoints> results = manager.createQuery(query, DataPoints.class).setMaxResults(max);
        return results.getResultList();
    }

}

