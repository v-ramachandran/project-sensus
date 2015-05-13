import sys
import getopt
import numpy as np
import collections
import random
from space import Space
from utils import read_dict, train_tm, train_tm_model, apply_tm, apply_tm_model, get_valid_data, score
from sklearn import linear_model

def usage(errno=0):
    print >>sys.stderr,\
    """
    Given train data (pairs of words and their translation), source language and 
    target language vectors, it outputs a translation matrix between source and 
    target spaces.

    Usage:
    python train_tm.py [options] train_data source_vecs target_vecs 
    \n\
    Options:
    -o --output <file>: output file prefix. Optional. Default is ./tm
    -h --help : help

    Arguments:
    train_data: <file>, train dictionary, list of word pairs (space separated words, 
            one word pair per line)
    source_vecs: <file>, vectors in source language. Space-separated, with string 
                identifier as first column (dim+1 columns, where dim is the dimensionality
                of the space)
    target_vecs: <file>, vectors in target language
    test_data: <file>, list of source-target word pairs (space separated words, 
                one word pair per line)


    Example:
    python train_tm.py train_data.txt ENspace.pkl ITspace.pkl

    """
    sys.exit(errno)


def main(sys_argv):

    try:
        opts, argv = getopt.getopt(sys_argv[1:], "ho:",
                                   ["help", "output="])
    except getopt.GetoptError, err:
        print str(err)
        usage()
        sys.exit(1)
    
    out_file = "./tm"
    additional = None
    for opt, val in opts:
        if opt in ("-o", "--output"):
            out_file = val
        if opt in ("-c", "--correction"):
            try:
                additional = int(val)
            except ValueError:
                usage(1)
        elif opt in ("-h", "--help"):
            usage(0)
        else:
            usage(1)

    if len(argv) == 5:
        source_file = argv[1]	
        target_file = argv[2]
        test_file = argv[3]
        model = eval(argv[4])
	dict_file = argv[0]
    else:
	print str(err)
	usage(1)


    print "Reading the training data"
    train_data = read_dict(dict_file)
    print train_data
    #we only need to load the vectors for the words in the training data
    #semantic spaces contain additional words
    source_words, target_words = zip(*train_data)

    print "Reading: %s" % source_file
    source_sp = Space.build(source_file, set(source_words))
    source_sp.normalize()

    print "Reading: %s" % target_file
    target_sp = Space.build(target_file, set(target_words))
    target_sp.normalize()

    print "Learning the translation matrix"
    tm = train_tm_model(source_sp, target_sp, train_data, model)

    #print "Printing the translation matrix"
    #np.savetxt("%s.txt" % out_file, tm)
    	
    print "Reading the test data"
    test_data = read_dict(test_file)

    #in the _source_ space, we only need to load vectors for the words in test.
    #semantic spaces may contain additional words, ALL words in the _target_ 
    #space are used as the search space
    source_words, _ = zip(*test_data)
    source_words = set(source_words)

    print "Reading: %s" % source_file
    if not additional:
        source_sp = Space.build(source_file, source_words)
    else:
        #read all the words in the space
        lexicon = set(np.loadtxt(source_file, skiprows=1, dtype=str, 
                                    comments=None, usecols=(0,)).flatten())
        #the max number of additional+test elements is bounded by the size 
        #of the lexicon
        additional = min(additional, len(lexicon) - len(source_words))
        #we sample additional elements that are not already in source_words
        random.seed(100)
        lexicon = random.sample(list(lexicon.difference(source_words)), additional)
        
        #load the source space
        source_sp = Space.build(source_file, source_words.union(set(lexicon)))
    
    source_sp.normalize()

    print "Reading: %s" % target_file
    target_sp = Space.build(target_file)
    target_sp.normalize()

    print "Translating" #translates all the elements loaded in the source space
    mapped_source_sp = apply_tm_model(source_sp, tm)
    
    print "Retrieving translations"
    test_data = get_valid_data(source_sp, target_sp, test_data)

    #turn test data into a dictionary (a word can have mutiple translation)
    gold = collections.defaultdict(set)
    for k, v in test_data:
        gold[k].add(v)

    score(mapped_source_sp, target_sp, gold, additional)

    print "Printing mapped vectors: %s" % out_file
    np.savetxt("%s.vecs.txt" % out_file, mapped_source_sp.mat)
    np.savetxt("%s.wds.txt" % out_file, mapped_source_sp.id2row, fmt="%s")

if __name__ == '__main__':
    main(sys.argv)

