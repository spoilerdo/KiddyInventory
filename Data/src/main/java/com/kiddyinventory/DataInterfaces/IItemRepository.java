package com.kiddyinventory.DataInterfaces;

import com.kiddyinventory.Entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface IItemRepository extends CrudRepository<Item, Integer> {
}
