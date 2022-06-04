package com.hit.aircraft_war.store.dao;

import java.util.List;

public interface RankDao {

    List<RankMember> getAllInformation();

    void doAdd(RankMember rankMember);

    void doDelete(String time);

    void  doSort();

    RankMember doSearch(String time);
}
