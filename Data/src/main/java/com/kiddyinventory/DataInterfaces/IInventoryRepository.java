package com.kiddyinventory.DataInterfaces;

import com.kiddinventory.Entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface IInventoryRepository extends CrudRepository<Item, Integer> {
}
