package org.yearup.data.mysql;

import org.yearup.models.Order;

public interface IorderDao {

    Order checkOut(int userId);
}
