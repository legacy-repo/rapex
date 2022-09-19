#!/usr/bin/env python3

import duckdb
# read_only=True
conn = duckdb.connect("./db/rapex_pathway.duckdb")

results = conn.execute("CREATE TABLE kegg_pathway AS SELECT * FROM read_csv_auto('./data/pathways.tsv');")

conn.close()

# Other data
