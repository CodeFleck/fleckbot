package br.com.codefleck.tradebot.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.codefleck.tradebot.exchanges.trading.api.impl.LogEntryImpl;
import br.com.codefleck.tradebot.models.PaginatedList;

@Repository
public class LogEntryImplDao {

    public LogEntryImplDao(){
        super();
    }

    @PersistenceContext
    private EntityManager manager;

    public List<LogEntryImpl> all()
    {
        return manager.createQuery("select l from LogEntryImpl l", LogEntryImpl.class).getResultList();
    }

    public void save(LogEntryImpl LogEntryImpl)
    {
        manager.persist(LogEntryImpl);
    }

    public LogEntryImpl findById(Integer id)
    {
        return manager.find(LogEntryImpl.class, id);
    }

    public void remove(LogEntryImpl LogEntryImpl)
    {
        manager.remove(LogEntryImpl);
    }

    public void update(LogEntryImpl LogEntryImpl)
    {
        manager.merge(LogEntryImpl);
    }

    public PaginatedList paginated(int page, int max) { return new PaginatorQueryHelper().list(manager, LogEntryImpl.class, page, max); }

}
