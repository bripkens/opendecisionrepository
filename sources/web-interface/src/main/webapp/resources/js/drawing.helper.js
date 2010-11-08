/*
 * Author: Ben Ripkens <bripkens.dev@gmail.com>
 */

function extend(child, supertype){
   child.prototype.__proto__ = supertype.prototype;
}