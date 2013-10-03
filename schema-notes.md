
# Notes on modeling synchronisms in ancient chronographic sources #

All datings are synchronisms.  These notes describe this project's approach to modeling synchronisms in our sources (and therefore, ultimately, modern dating) using the CITE architecture.

For our purposes, *events* are limited to events defined by our sources, and are represented by a simple collection, including one attestation.

The key object to model is the *synchronism*.  A synchronism relates an event to one of:

- another event
- an epoch count (e.g. regnal sequences used in sources like Ptolemy's *Canon of Kings*, or the unique epoch of the Parian Marble)
- an eponymous list (e.g. Athenian archons or Roman consuls)
- a chronological cycle (e.g. Olympiads)

Each of these related objects must be modeled.

## Events ##

Events can be related directory to other events by CITE URN values.

## Eponymous lists

A simple collection for each system of eponymous lists URNs and labels for valid items.  The collection should be *unordered* since sequencing of the eponyms will be derived algorithmically from synchronisms.

## Epoch count ##


## Cycle ##




Topological relations can include:

- contemporary with
- precedes/follows
- precedes/follows by some units in a cycle, epoch count, or eponymous list






## Other collections


Collection of (attributed) synchronisms


csv format looks like this:

    urn:cite:chron:synchronism.1,CTSURN,EVENTURN,"[synchr,precedes,follows]","[even\
    t/cycle/epon/epoch]",SYNCURN,"{number}"








