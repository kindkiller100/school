package com.school.school.groups;

import org.springframework.beans.factory.annotation.Autowired;
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
        if (checkIfExists(id)) {
            groupRepository.deleteById(id);
        }
    }

    private boolean checkIfExists(long id) {
        if (groupRepository.existsById(id)) {
            return true;
        }

        throw new NotFoundException("Group with id «" + id + "» not found.");
    }

    private Group getIfExists(long id) {
        return groupRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Group with id «" + id + "» not found."));
    }
}
