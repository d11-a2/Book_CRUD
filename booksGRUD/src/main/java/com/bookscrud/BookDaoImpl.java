package com.bookscrud;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.List;


@Transactional
@Repository("bookDao")
public class BookDaoImpl implements BookDao {

    private static final Log LOG = LogFactory.getLog(BookDaoImpl.class);

    private SessionFactory sessionFactory;

    @Resource(name = "sessionFactory")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<Book> findByTitle(String title) {
        if (title == null || title == "")
            return null;

        Query query = sessionFactory.getCurrentSession().getNamedQuery("searchByTitle");
        query.setParameter("title", title);

        return query.getResultList();
    }


    public List<Book> findByYear(Integer year) {
        if (year <= 0)
            return null;

        Query query = sessionFactory.getCurrentSession().getNamedQuery("searchByYear");
        query.setParameter("printYear", year);

        return query.getResultList();
    }

    public List<Book> findByAuthor(String author) {
        if (author == null || author == "")
            return null;

        Query query = sessionFactory.getCurrentSession().getNamedQuery("searchByAuthor");
        query.setParameter("author", author);

        return query.getResultList();
    }

    public List<Book> findByISBN(String ISBN) {

        Query query = sessionFactory.getCurrentSession().getNamedQuery("searchByISBN");
        query.setParameter("isbn", ISBN);

        return query.getResultList();
    }


    public List<Book> searchByReadAlready(Boolean readAlready) {

        Query query = sessionFactory.getCurrentSession().getNamedQuery("searchByReadAlready");
        query.setParameter("readAlready", readAlready);

        return query.getResultList();
    }


    public Book read(Book book) {
        book.setReadAlready(true);
        LOG.info("Read book with id: " + book.getId() + " ...");
        return update(book);
    }

    public Book changeEdition(Book book) {
        book.setReadAlready(false);
        update(book);
        return book;
    }

    private Book update(Book book) {
        sessionFactory.getCurrentSession().update(book);
        LOG.info("Book updated with id: " + book.getId());
        return book;
    }

    public Book create(Book book) {
        sessionFactory.getCurrentSession().save(book);
        LOG.info("Book created with id: " + book.getId());
        return book;
    }

    public void delete(Book book) {
        sessionFactory.getCurrentSession().delete(book);
        LOG.info("Book deleted with id: ");
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return sessionFactory.getCurrentSession().createQuery("select b from Book b").list();
    }

    public Book findById(long id) {
        return (Book) sessionFactory.getCurrentSession().getNamedQuery("searchById")
                .setParameter("id", id).uniqueResult();
    }


}