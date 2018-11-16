package com.kiddyinventory.LogicTests;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.Helper.RestCallHelper;
import com.kiddyinventory.Logic.InventoryLogic;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;
import static com.kiddyinventory.Constants.APIConstants.*;

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
    @Mock
    private RestCallHelper restCallHelper;

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
        Account dummyAccount = new Account();

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));

        when(accountContext.findById(any(Integer.class))).thenReturn(Optional.ofNullable(dummyAccount));
        when(inventoryContext.findById(any(Integer.class))).thenReturn(Optional.of(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.saveItem(dummyPrincipal, dummyAccount.getAccountID(), dummyItem.getItemID());

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
        Account dummyAccount = new Account();

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummyItem.getAccounts().add(dummyAccount);

        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        Item itemFromDb = _logic.getItem(dummyPrincipal, dummyItem.getItemID());

        Assert.assertEquals(dummyItem, itemFromDb);
    }

    @Test
    public void TestGetItemUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        Account dummyAccount = new Account();
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.getItem(dummyPrincipal, 0);
    }

    @Test
    public void TestGetItemsFromAccountValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        Item dummy1Item = new Item("test1item", "test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("test2item", "test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountContext.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        List<Item> itemsFromDb = _logic.getItemsFromAccount(dummyPrincipal, dummyAccount.getAccountID());

        Assert.assertEquals(dummyItems, itemsFromDb);
    }

    @Test
    public void TestGetItemsFromAccountUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountContext.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.getItemsFromAccount(dummyPrincipal, dummyAccount.getAccountID());
    }

    @Test
    public void TestDeleteItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummyItem.getAccounts().add(dummyAccount);

        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.deleteItem(dummyPrincipal, dummyItem.getItemID());

        verify(inventoryContext, times(1)).delete(dummyItem);
    }

    @Test
    public void TestDeleteItemUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItem(dummyPrincipal, dummyItem.getItemID());
    }

    @Test
    public void TestDeleteItemsFromAccountValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        Item dummy1Item = new Item("dummy1Item", "test item", Condition.FN, 10.50f);
        Item dummy2Item = new Item("dummy2Item", "test item", Condition.FN, 10.50f);

        List<Item> dummyItems = new ArrayList<>();
        dummyItems.add(dummy1Item);
        dummyItems.add(dummy2Item);
        dummyAccount.setItems(dummyItems);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountContext.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.deleteItemsFromAccount(dummyPrincipal, dummyAccount.getAccountID());

        verify(inventoryContext, times(1)).deleteAll(dummyAccount.getItems());
    }

    @Test
    public void TestDeleteItemsFromAccountUnvalid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";
        Account dummyAccount = new Account();

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountContext.findById(dummyAccount.getAccountID())).thenReturn(Optional.ofNullable(dummyAccount));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.deleteItemsFromAccount(dummyPrincipal, dummyAccount.getAccountID());
    }

    @Test
    public void TestMoveItemValid(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        dummy2Account.setAccountID(1);

        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);
        dummy1Account.getItems().add(dummyItem);
        dummyItem.getAccounts().add(dummy1Account);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));
        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));
        when(accountContext.findById(dummy1Account.getAccountID())).thenReturn(Optional.ofNullable(dummy1Account));
        when(accountContext.findById(dummy2Account.getAccountID())).thenReturn(Optional.ofNullable(dummy2Account));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        _logic.moveItem(dummyPrincipal,dummy1Account.getAccountID(), dummy2Account.getAccountID(), dummyItem.getItemID());

        Assert.assertTrue(!dummy2Account.getItems().isEmpty());
    }

    @Test
    public void TestMoveItemNotInDb(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));
        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummyPrincipal, dummy1Account.getAccountID(), dummy2Account.getAccountID(), dummyItem.getItemID());
    }

    @Test
    public void TestMoveItemSenderDoesntContainItem(){
        Principal dummyPrincipal = Mockito.mock(Principal.class);
        String principalName = "name";

        Account dummy1Account = new Account();
        Account dummy2Account = new Account();
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(restCallHelper.getCall(GET_BANKACCOUNT + principalName, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));
        when(restCallHelper.getCall(GET_BANKACCOUNT, Account.class)).thenReturn(ResponseEntity.ok(dummy1Account));

        when(inventoryContext.findById(dummyItem.getItemID())).thenReturn(Optional.ofNullable(dummyItem));

        when(dummyPrincipal.getName()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);
        _logic.moveItem(dummyPrincipal, dummy1Account.getAccountID(), dummy2Account.getAccountID(), dummyItem.getItemID());
    }
}
