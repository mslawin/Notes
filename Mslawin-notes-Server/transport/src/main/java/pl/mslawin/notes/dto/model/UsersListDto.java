package pl.mslawin.notes.dto.model;

import java.util.Collection;

public class UsersListDto {

    private Collection<String> users;

    public UsersListDto() {
    }

    public UsersListDto(Collection<String> users) {
        this.users = users;
    }

    public Collection<String> getUsers() {
        return users;
    }

    public void setUsers(Collection<String> users) {
        this.users = users;
    }
}