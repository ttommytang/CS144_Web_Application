#!/bin/bash
mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

sort -u  ItemInfo.dat -o ItemInfo.dat
sort -u CategoryInfo.dat -o CategoryInfo.dat
sort -u BidsInfo.dat -o BidsInfo.dat
sort -u UserInfo.dat -o UserInfo.dat
sort -u SellerInfo.dat -o SellerInfo.dat

mysql CS144 < load.sql

rm *.dat

