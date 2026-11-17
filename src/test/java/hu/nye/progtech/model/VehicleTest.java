package hu.nye.progtech.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void getCellsShouldReturnCellsOfHorizontalVehicle() {
        Vehicle vehicle = new Vehicle('A', new Position(2, 1), 3, Orientation.HORIZONTAL);

        assertEquals(List.of(new Position(2, 1), new Position(2, 2), new Position(2, 3)), vehicle.getCells());
    }

    @Test
    void getCellsShouldReturnCellsOfVerticalVehicle() {
        Vehicle vehicle = new Vehicle('C', new Position(0, 4), 2, Orientation.VERTICAL);

        assertEquals(List.of(new Position(0, 4), new Position(1, 4)), vehicle.getCells());
    }

    @Test
    void moveByShouldReturnNewVehicleAndKeepTheOriginal() {
        Vehicle vehicle = new Vehicle('C', new Position(1, 4), 2, Orientation.VERTICAL);

        Vehicle moved = vehicle.moveBy(-1);

        assertEquals(new Position(0, 4), moved.getStart());
        assertEquals(new Position(1, 4), vehicle.getStart());
    }

    @Test
    void equalVehiclesShouldHaveSameHashCode() {
        Vehicle first = new Vehicle('A', new Position(2, 1), 2, Orientation.HORIZONTAL);
        Vehicle second = new Vehicle('A', new Position(2, 1), 2, Orientation.HORIZONTAL);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, first.moveBy(1));
    }
}
