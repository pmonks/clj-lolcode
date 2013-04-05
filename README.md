# lolcmis

An interpreter for the LOLCMIS (LOLCODE + CMIS) programming language.  See http://blogs.alfresco.com/wp/developer/2013/04/01/alfresco-announces-hipster-compatible-enterprise-content-management-sdk/.

## Installation

Checkout the source from https://github.com/pmonks/lolcmis

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

## Build Status

https://travis-ci.org/pmonks/lolcmis

## Bugs

https://github.com/pmonks/lolcmis/issues

## License

Copyright © 2013 Peter Monks (pmonks@gmail.com)

Distributed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
