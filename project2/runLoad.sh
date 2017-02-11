#!/bin/bash

ant
ant -f build.xml run-all

#eliminate duplication
sort items.dat | uniq > items_done.dat
sort sellers.dat | uniq > sellers_done.dat
sort bidders.dat | uniq > bidders_done.dat
sort bids.dat | uniq > bids_done.dat
sort categories.dat | uniq > categories_done.dat

mysql CS144 < drop.sql
mysql CS144 < create.sql
mysql CS144 < load.sql

rm *.dat
