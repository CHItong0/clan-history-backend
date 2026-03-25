package com.clanhistory.service;

import com.clanhistory.entity.Person;
import java.util.List;

public interface PersonService {
    List<Person> findAll();
    Person findById(Long id);
    boolean save(Person person);
    boolean updateById(Person person);
    boolean deleteById(Long id);
    List<Person> findByFamilyId(Long familyId);
    List<Person> findByStatus(Integer status);
    List<Person> buildGenealogyTree();
}
