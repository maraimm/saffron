Saffron 3 - Text Analysis and Insight Tool
==========================================

Saffron is a tool for providing multi-stage analysis of text corpora by means 
of state-of-the-art natural language processing technology. Saffron consists of
a set of self-contained and independent modules that individually provide 
distinct analysis of text. These modules are as follows

1. *Corpus Indexing*: Analyses raw text documents in various formats and indexs
them for later components
2. *Domain Model*: Identifies the keywords that define the domain of the corpus
3. *Topic Extraction*: Extracts keyphrase that are the topics of each single
document in a collection
4. *Author Consolidation*: Detects and removes name variations from the list 
of authors of each document
5. *DBpedia Lookup*: Links topics extracted from a document to URLs on the 
Semantic Web
6. *Topic Statistics*: Analyses the topics of a document and finds the relative
importance of these topics
7. *Connect Authors*: Associates authors with particular documents and 
identifies the importance of the document to each authro
8. *Topic Similarity*: Measures the relevance of each topic to each other topic
9. *Author Similarity*: Measures the relevance of each author to each other
author
10. *Taxonomy Extraction*: Organizes the topics into a single hierarchical 
graph that allows for easy browsing of the corpus and deep insights.

Installation
------------

Saffron requires the use of [Maven](https://maven.apache.org/) to install and 
can be built with the following commande

    mvn install

Saffron depends on some number of resources and these can be obtained with the
following script

    ./install.sh

Alternatively Saffron can be used in a Docker image as follows

    docker pull insightcentre/saffron
    docker run -it insightcentre/saffron bash

Running
-------

All steps of Saffron can be executed by running the `saffron.sh` script. This 
script takes two arguments

1. The corpus, which may be 
    1. A folder containing files in TXT, DOC or PDF
    2. A zip file containing files in TXT, DOC or PDF
    3. A Json metadata file describing the corpus (see [Saffron Formats](FORMATS.md))
2. The output folder to which the results are written

For example

    ./saffron.sh corpus.json output/

The following results are generated

* `corpus-unconsolidated.json`: The unconsolidated corpus metadata
* `domain-model.json`: The domain model (domain specific keywords)
* `topics-extracted.json`: The initial unfiltered list of extracted topics
* `doc-topics-extracted.json`: The connection of these topics to documents
* `corpus.json`: The corpus metadata
* `topics-dbpedia.json`: The topics with DBpedia links
* `doc-topics.json`: The document topic map with weights
* `topics.json`: The topics with weights (and DBpedia links)
* `author-topics.json`: The connection between authors and topics
* `topic-sim.json`: The topic-topic similarity graph
* `author-sim.json`: The author-author similarity graph
* `taxonomy.json`: The final taxonomy over the corpus
