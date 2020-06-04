package com.kiddyinventory.LogicTests;

import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.Logic.ItemLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemLogicTest {

    //Add exception handler
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IItemRepository itemRepository;

    @InjectMocks
    private ItemLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateItemValid() {
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(itemRepository.save(dummyItem)).thenReturn(dummyItem);

        Item returnedItem = _logic.createItem(dummyItem);

        //Check if the right item is returned
        Assert.assertEquals(dummyItem, returnedItem);
        //Check if the save function is called in the repo
        verify(itemRepository, times(1)).save(dummyItem);
    }

    @Test
    public void testCreateItemInvalid() {
        Item dummyItem = new Item("", "", null, 10.50f);

        exception.expect(IllegalArgumentException.class);

        _logic.createItem(dummyItem);
    }

    @Test
    public void testDeleteItemValid() {
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(dummyItem));

        _logic.deleteItem(dummyItem.getItemID());

        verify(itemRepository, times(1)).delete(dummyItem);
    }

    @Test
    public void testDeleteItemInvalid() {
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.deleteItem(1);
    }

    @Test
    public void testUpdateItemValid() {
        Item oldItem = new Item("dummyOldItem", "test item", Condition.FN, 10.50f);
        Item newItem = new Item("dummyNewItem","test item", Condition.FN, 10.50f);

        when(itemRepository.findById(oldItem.getItemID())).thenReturn(Optional.of(oldItem));

        _logic.updateItem(newItem);

        verify(itemRepository, times(1)).save(newItem);
    }

    @Test
    public void testUpdateItemInvalid() {
        Item newItem = new Item("dummyNewItem","test item", Condition.FN, 10.50f);

        when(itemRepository.findById(newItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.updateItem(newItem);
    }

    @Test
    public void testGetItemExists() {
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(itemRepository.findById(dummyItem.getItemID())).thenReturn(Optional.of(dummyItem));

        Assert.assertEquals(dummyItem, _logic.getItem(dummyItem.getItemID()));
        verify(itemRepository, times(1)).findById(dummyItem.getItemID());
    }

    @Test
    public void testGetItemDoesNotExist() {
        Item dummyItem = new Item("dummyItem", "test item", Condition.FN, 10.50f);

        when(itemRepository.findById(dummyItem.getItemID())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);
        _logic.getItem(dummyItem.getItemID());
    }

}
