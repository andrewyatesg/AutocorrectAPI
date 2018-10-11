# AutocorrectWebsite
API for autocorrecting words

Uses the Levenshtein metric for calculating the distance between strings. The tree structure can be visualized in two dimensional "string space" where each node is a point. The subtree of each child of a particular node X lies within a circle of radius R, where R is the Levenshtein distance between X and all the nodes in the subtree. The algorithm descends into subtrees that fall within a particular distance (our tolerance level) to the word we want autocorrected.  

The algorithm mentioned above is found in the `util` package.
