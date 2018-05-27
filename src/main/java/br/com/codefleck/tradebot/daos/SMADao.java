package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.core.util.SMA;
import br.com.codefleck.tradebot.core.util.SMAUtil;
import br.com.codefleck.tradebot.models.PaginatedList;

@Repository
public class SMADao {

        @PersistenceContext
        private EntityManager manager;

        public List<SMA> all()
        {
            return manager.createQuery("select s from SMA s", SMA.class).getResultList();
        }

        public void save(SMA sma)
        {
            manager.persist(sma);
        }

        public SMA findById(Integer id)
        {
            return manager.find(SMA.class, id);
        }

        public void remove(SMA sma)
        {
            manager.remove(sma);
        }

        public void update(SMA sma)
        {
            manager.merge(sma);
        }

        public PaginatedList paginated(int page, int max)
        {
            return new PaginatorQueryHelper().list(manager, SMA.class, page, max);
        }

    }

