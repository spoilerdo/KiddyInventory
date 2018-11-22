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

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InventoryLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IItemRepository inventoryContext;
    @Mock
    private IAccountRepository accountContext;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private InventoryLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestSaveItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account (principalName);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(accountContext.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.of(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.saveItem(dummyPrincipal, dummyAccount.getId(), dummyItem.getItemID());

        verify(accountContext, times(1)).save(dummyAccount);
    }

    @Test
    public void TestSaveItemUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.saveItem(dummyPrincipal, 0, 0);
    }

    @Test
    public void TestGetItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummyItem.getAccounts().add(dummyAccount);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        Item itemFromDb = _logic.getItem(dummyPrincipal, dummyItem.getItemID());

        Assert.assertEquals(dummyItem, itemFromDb);
    }

    @Test
    public void TestGetItemUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.getItem(dummyPrincipal, 0);
    }

    @Test
    public void TestGetItemsFromAccountValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        Item dummy1Item = new Item("test1item", "test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("test2item", "test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(accountContext.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        List<Item> itemsFromDb = _logic.getItemsFromAccount(dummyPrincipal, dummyAccount.getId());

        Assert.assertEquals(dummyItems, itemsFromDb);
    }

    @Test
    public void TestGetItemsFromAccountUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(accountContext.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.getItemsFromAccount(dummyPrincipal, dummyAccount.getId());
    }

    @Test
    public void TestDeleteItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummyItem.getAccounts().add(dummyAccount);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.deleteItem(dummyPrincipal, dummyItem.getItemID());

        verify(inventoryContext, times(1)).delete(dummyItem);
    }

    @Test
    public void TestDeleteItemUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItem(dummyPrincipal, dummyItem.getItemID());
    }

    @Test
    public void TestDeleteItemsFromAccountValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        Item dummy1Item = new Item("dummy1Item", "test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("dummy2Item", "test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(accountContext.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.deleteItemsFromAccount(dummyPrincipal, dummyAccount.getId());

        verify(inventoryContext, times(1)).deleteAll(dummyAccount.getItems());
    }

    @Test
    public void TestDeleteItemsFromAccountUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account(principalName);

        when(accountContext.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(accountContext.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItemsFromAccount(dummyPrincipal, dummyAccount.getId());
    }

    @Test
    public void TestMoveItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account(principalName);
        Account dummy2Account = new Account("dummy2");
        dummy2Account.setId(1);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummy1Account.getItems().add(dummyItem);
        dummyItem.getAccounts().add(dummy1Account);

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(accountContext.findByUsername(dummy1Account.getUsername())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountContext.findByUsername(dummy2Account.getUsername())).thenReturn(Optional.ofNullable(dummy2Account));

        when(accountContext.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountContext.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.moveItem(dummyPrincipal,dummy1Account.getId(), dummy2Account.getId(), dummyItem.getItemID());

        Assert.assertTrue(!dummy2Account.getItems().isEmpty());
    }

    @Test
    public void TestMoveItemNotInDb(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account(principalName);
        Account dummy2Account = new Account("dummy2");
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(accountContext.findByUsername(dummy1Account.getUsername())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountContext.findByUsername(dummy2Account.getUsername())).thenReturn(Optional.ofNullable(dummy2Account));

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummyPrincipal, dummy1Account.getId(), dummy2Account.getId(), dummyItem.getItemID());
    }

    @Test
    public void TestMoveItemSenderDoesntContainItem(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account(principalName);
        Account dummy2Account = new Account("dummy2");
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(accountContext.findByUsername(dummy1Account.getUsername())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountContext.findByUsername(dummy2Account.getUsername())).thenReturn(Optional.ofNullable(dummy2Account));

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummyPrincipal, dummy1Account.getId(), dummy2Account.getId(), dummyItem.getItemID());
    }
}
