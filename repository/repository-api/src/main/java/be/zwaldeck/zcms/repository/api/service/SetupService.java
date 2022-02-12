package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.exception.RepositoryException;

public interface SetupService {
    boolean isRepositorySetup() throws RepositoryException;
    void setupRepository() throws RepositoryException;
}
