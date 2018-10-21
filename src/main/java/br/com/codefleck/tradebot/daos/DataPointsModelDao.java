package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.core.util.PaginatedList;
import br.com.codefleck.tradebot.models.DataPointsModel;

@Repository
public class DataPointsModelDao {

    @PersistenceContext
    private EntityManager manager;

    public List<DataPointsModel> all()
    {
        return manager.createQuery("select dp from DataPointsModel dp", DataPointsModel.class).getResultList();
    }

    public void save(DataPointsModel dataPointsModel)
    {
        manager.persist(dataPointsModel);
    }

    public DataPointsModel findById(Integer id)
    {
        return manager.find(DataPointsModel.class, id);
    }

    public void remove(DataPointsModel dataPointsModel)
    {
        manager.remove(dataPointsModel);
    }

    public void update(DataPointsModel dataPointsModel)
    {
        manager.merge(dataPointsModel);
    }

    public PaginatedList paginated(int page, int max)
    {
        return new PaginatorQueryHelper().list(manager, DataPointsModel.class, page, max);
    }

}

