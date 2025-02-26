package id.ac.ui.cs.advprog.eshop.repository;

public interface InterfaceRepositoryWrite<T> {
    public T create(T obj);
    public T edit(T newObj);
    public boolean delete(String id);
}