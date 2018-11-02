package com.kiddyinventory.LogicTests;

import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.Logic.ItemLogic;
import com.kiddyinventory.LogicInterface.IItemLogic;
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
    private ItemLogic itemLogic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateItemValid() {
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(itemRepository.save(dummyItem)).thenReturn(dummyItem);

        Item returnedItem = itemLogic.createItem(dummyItem);

        //Controleren of juiste item is gereturned
        Assert.assertEquals(dummyItem, returnedItem);
        //Controleren of repository save functie is aangeroepen
        verify(itemRepository, times(1)).save(dummyItem);
    }

    @Test
    public void testCreateItemInvalid() {
        Item dummyItem = new Item("", "", null, 10.50f);

        exception.expect(IllegalArgumentException.class);

        itemLogic.createItem(dummyItem);
    }

    @Test
    public void testDeleteItemValid() {
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(dummyItem));

        itemLogic.deleteItem(1);

        verify(itemRepository, times(1)).delete(dummyItem);
    }

    @Test
    public void testDeleteItemInvalid() {
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        itemLogic.deleteItem(1);
    }

    @Test
    public void testUpdateItemValid() {
        Item oldItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);
        Item newItem = new Item("testitemnieuw","dit is een test item", Condition.FN, 10.50f);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(oldItem));

        itemLogic.updateItem(newItem);

        verify(itemRepository, times(1)).save(newItem);

    }

    @Test
    public void testUpdateItemInvalid() {
        Item newItem = new Item("testitemnieuw","dit is een test item", Condition.FN, 10.50f);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        exception.expect(IllegalArgumentException.class);

        itemLogic.updateItem(newItem);
    }

    @Test
    public void testGetItemExists() {
        Item dummyItem = new Item("testitem", "dit is een test item", Condition.FN, 10.50f);

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(dummyItem));

        Assert.assertEquals(dummyItem, itemLogic.getItem(1));
        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    public void testGetItemDoesNotExist() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        exception.expect(IllegalArgumentException.class);

        itemLogic.getItem(1);
    }

}
