package com.kiddyinventory.LogicTests;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.Logic.InventoryLogic;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InventoryLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IItemRepository inventoryRepository;
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
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(accountRepository.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        _logic.saveItem(dummyAccount, dummyItem);

        verify(inventoryRepository, times(1)).save(dummyItem);
    }

    @Test
    public void TestSaveItemUnvalid(){
        Account dummyAccount = new Account();
        Item dummyItem = new Item("", "dit is een test item", Condition.FN, 0f);

        exception.expect(IllegalArgumentException.class);
        _logic.saveItem(dummyAccount, dummyItem);

        verify(inventoryRepository, times(1)).save(dummyItem);
    }

    @Test
    public void TestGetItemValid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        Item itemFromDb = _logic.getItem(dummyItem.getItemID());

        Assert.assertEquals(dummyItem, itemFromDb);
    }

    @Test
    public void TestGetItemUnvalid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.getItem(dummyItem.getItemID());
    }

    @Test
    public void TestGetItemsFromAccountValid(){
        Account dummyAccount = new Account();
        Item dummy1Item = new Item("test1item", "dit is een test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("test2item", "dit is een test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(accountRepository.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        List<Item> itemsFromDb = _logic.getItemsFromAccount(dummyAccount.getId());

        Assert.assertEquals(dummyItems, itemsFromDb);
    }

    @Test
    public void TestGetItemsFromAccountUnvalid(){
        Account dummyAccount = new Account();

        when(accountRepository.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        exception.expect(IllegalArgumentException.class);
        _logic.getItemsFromAccount(dummyAccount.getAccountID());
    }

    @Test
    public void TestDeleteItemValid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        _logic.deleteItem(dummyItem.getItemID());

        verify(inventoryRepository, times(1)).delete(dummyItem);
    }

    @Test
    public void TestDeleteItemUnvalid(){
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItem(dummyItem.getItemID());
    }

    @Test
    public void TestDeleteItemsFromAccountValid(){
        Account dummyAccount = new Account();
        Item dummy1Item = new Item("test1item", "dit is een test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("test2item", "dit is een test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(accountRepository.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        _logic.deleteItemsFromAccount(dummyAccount.getAccountID());

        verify(inventoryRepository, times(1)).deleteAll(dummyAccount.getItems());
    }

    @Test
    public void TestDeleteItemsFromAccountUnvalid(){
        Account dummyAccount = new Account();

        when(accountRepository.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItemsFromAccount(dummyAccount.getAccountID());
    }

    @Test
    public void TestMoveItemValid(){
        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        dummy2Account.setId(1);
        Item dummyItem = new Item("test1item", "dit is een test item", Item.Condition.FN, 10.50f);
        dummy1Account.setItems(new ArrayList<>(Arrays.asList(dummyItem)));

        when(inventoryRepository.findById(dummyItem.getId())).thenReturn(Optional.ofNullable(dummyItem));
        when(accountRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        _logic.moveItem(dummy1Account.getId(), dummy2Account.getId(), dummyItem);
    }

    @Test
    public void TestMoveItemNotInDb(){
        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        Item dummyItem = new Item("test1item", "dit is een test item", Item.Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummy1Account.getId(), dummy2Account.getId(), dummyItem);
    }

    @Test
    public void TestMoveItemSenderDoesntContainItem(){
        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        Item dummyItem = new Item("test1item", "dit is een test item", Item.Condition.FN, 10.50f);

        when(inventoryRepository.findById(dummyItem.getId())).thenReturn(Optional.ofNullable(dummyItem));
        when(accountRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummy1Account.getId(), dummy2Account.getId(), dummyItem);
    }
}
