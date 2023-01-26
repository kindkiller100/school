package com.school.school.groups;

import com.school.school.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public void create(Group group) {
        groupRepository.save(group);
    }

    public void edit(Group group) {
        Group groupClone = getIfExists(group.getId())
                .clone()
                .setTitle(group.getTitle())
                .build();

        groupRepository.save(groupClone);
    }

    public void delete(long id) {
        checkIfExists(id);
        groupRepository.deleteById(id);
    }

    private void checkIfExists(long id) {
        if (!groupRepository.existsById(id)) {
            throw new ValidationException("id", "Группа с id «" + id + "» не найдена.").setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private Group getIfExists(long id) {
        return groupRepository.getIfExists(id);
    }
}
