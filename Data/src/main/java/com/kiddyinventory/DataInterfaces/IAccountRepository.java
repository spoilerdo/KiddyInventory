package com.kiddyinventory.DataInterfaces;

import com.kiddinventory.Entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
}
