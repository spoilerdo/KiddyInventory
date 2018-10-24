package com.kiddyinventory.DataInterfaces;

import com.kiddyinventory.Entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface IInventoryRepository extends CrudRepository<Item, Integer> {
}
