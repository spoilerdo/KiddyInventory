package com.kiddyinventory.DataInterfaces;

import com.kiddyinventory.Entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
}
