#!/usr/bin/env python3

import duckdb
# read_only=True
conn = duckdb.connect("./db/rapex_degs.duckdb")

# Self-produced data
pmid = "000000"
organs = ["gut", "hrt", "kdn", "lng", "lvr", "tst", "tyr"]
methods = ["fpkm_ttest", "fpkm_wilcox", "tpm_ttest", "tpm_wilcox", "counts_limma"]

for method in methods:
    for organ in organs:
        id = "%s_%s_%s" % (organ, pmid, method)
        results = conn.execute("CREATE TABLE %s AS SELECT * FROM read_csv_auto('./data/degs/%s.csv');" % (id, id))

conn.close()

# Other data
