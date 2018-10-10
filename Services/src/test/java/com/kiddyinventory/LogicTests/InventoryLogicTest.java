package com.kiddyinventory.LogicTests;

import com.kiddinventory.Entities.Account;
import com.kiddinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IInventoryRepository;
import com.kiddyinventory.Logic.InventoryLogic;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InventoryLogicTest {

    //Add exception dependencie
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IInventoryRepository inventoryRepository;
    @Mock
    private IAccountRepository accountRepository;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private InventoryLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestSaveItemValid(){
        Account dummyAccount = new Account();
        Item dummyItem = new Item("testitem", "dit is een test item", Item.Condition.FN, 10.50f);

        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        _logic.saveItem(dummyAccount, dummyItem);

        verify(inventoryRepository, times(1)).save(dummyItem);
    }

    @Test
    public void TestSaveItemUnvalid(){
        Account dummyAccount = new Account();
        Item dummyItem = new Item("", "dit is een test item", Item.Condition.FN, 0f);

        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        exception.expect(IllegalArgumentException.class);
        _logic.saveItem(dummyAccount, dummyItem);

        verify(inventoryRepository, times(1)).save(dummyItem);
    }

    @Test
    public void TestGetItemValid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Item.Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getId())).thenReturn(Optional.ofNullable(dummyItem));

        Item itemFromDb = _logic.getItem(dummyItem.getId());

        Assert.assertEquals(dummyItem, itemFromDb);
    }

    @Test
    public void TestGetItemUnvalid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Item.Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.getItem(dummyItem.getId());
    }

    @Test
    public void TestGetItemsFromAccountValid(){
        Account dummyAccount = new Account();
        Item dummy1Item = new Item("test1item", "dit is een test item", Item.Condition.FN, 10.50f);
        Item dummy2Item = new Item("test2item", "dit is een test item", Item.Condition.FN, 10.50f);

        Set<Item> dummyItems = new HashSet<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        Set<Item> itemsFromDb = _logic.getItemsFromAccount(dummyAccount.getId());

        Assert.assertEquals(dummyItems, itemsFromDb);
    }

    @Test
    public void TestGetItemsFromAccountUnvalid(){
        Account dummyAccount = new Account();

        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        exception.expect(IllegalArgumentException.class);
        _logic.getItemsFromAccount(dummyAccount.getId());
    }
}
