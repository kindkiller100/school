package com.school.school.groups;

import com.school.school.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;
    private final ValidationException validationException = new ValidationException();

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
            validationException.clear();
            validationException.put("id", "Group with id «" + id + "» not found.");
            validationException.throwExceptionIfIsNotEmpty();
        }
    }

    private Group getIfExists(long id) {
        return groupRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Group with id «" + id + "» not found."));
    }
}
