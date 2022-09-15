#!/usr/bin/env python3

import duckdb
# read_only=True
conn = duckdb.connect("./db/rapex_expr.duckdb")

# Self-produced data
pmid = "000000"
organs = ["gut", "hrt", "kdn", "lng", "lvr", "tst", "tyr"]
methods = ["fpkm", "tpm", "counts"]

for method in methods:
    for organ in organs:
        id = "%s_%s_%s" % (organ, pmid, method)
        results = conn.execute("CREATE TABLE %s AS SELECT * FROM read_csv_auto('./data/expr/%s.csv');" % (id, id))

conn.close()

# Other data
