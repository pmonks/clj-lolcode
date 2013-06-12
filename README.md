# lolcmis

An interpreter for the LOLCMIS (LOLCODE + CMIS) programming language.  See [this press release](http://blogs.alfresco.com/wp/developer/2013/04/01/alfresco-announces-hipster-compatible-enterprise-content-management-sdk/).

## Installation

Checkout the source from [GitHub](https://github.com/pmonks/lolcmis).

## Running

    $ lein run -- <filename>

## Usage:

    Switches               Default  Desc
    --------               -------  ----
    -a, --no-ast, --ast    false    Print the AST for the program, instead of evaluating it.
    -h, --no-help, --help  false    Show help

    Args       Desc
    ----       ----
    filename   The filename of the LOLCMIS program to evaluate.


## Developer Information

[GitHub project](https://github.com/pmonks/lolcmis)

[Bug Tracker](https://github.com/pmonks/lolcmis/issues)

[Continuous Integration](https://travis-ci.org/pmonks/lolcmis) ![Build Status](https://api.travis-ci.org/pmonks/lolcmis.png)


## License

Copyright © 2013 Peter Monks (pmonks@gmail.com)

Distributed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
