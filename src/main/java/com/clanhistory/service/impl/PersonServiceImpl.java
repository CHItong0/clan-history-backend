package com.clanhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.entity.Person;
import com.clanhistory.mapper.PersonMapper;
import com.clanhistory.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonMapper personMapper;

    @Override
    public List<Person> findAll() {
        return personMapper.selectList(null);
    }

    @Override
    public List<Person> buildGenealogyTree() {
        // 获取所有未删除的人员
        List<Person> allPersons = personMapper.selectList(
            new LambdaQueryWrapper<Person>().eq(Person::getDeleted, 0)
        );

        // 构建 id -> Person 映射
        Map<Long, Person> personMap = allPersons.stream()
            .collect(Collectors.toMap(Person::getId, p -> p));

        // 初始化 children 列表
        personMap.values().forEach(p -> p.setChildren(new ArrayList<>()));

        // 建立父子关系
        List<Person> roots = new ArrayList<>();
        for (Person p : allPersons) {
            if (p.getFatherId() == null || !personMap.containsKey(p.getFatherId())) {
                roots.add(p);
            } else {
                Person father = personMap.get(p.getFatherId());
                father.getChildren().add(p);
            }
        }

        return roots;
    }

    @Override
    public Person findById(Long id) {
        return personMapper.selectById(id);
    }

    @Override
    public boolean save(Person person) {
        return personMapper.insert(person) > 0;
    }

    @Override
    public boolean updateById(Person person) {
        return personMapper.updateById(person) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return personMapper.deleteById(id) > 0;
    }

    @Override
    public List<Person> findByFamilyId(Long familyId) {
        return personMapper.selectList(new LambdaQueryWrapper<Person>().eq(Person::getFamilyId, familyId));
    }

    @Override
    public List<Person> findByStatus(Integer status) {
        return personMapper.selectList(new LambdaQueryWrapper<Person>().eq(Person::getIsCelebrity, status));
    }
}
