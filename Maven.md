<table cellpadding='5' border='1' cellspacing='0' width='650'>
<blockquote><thead>
<blockquote><tr>
<blockquote><th width='150'> Name </th>
<th>Maven</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td> <b>Problem/Issue</b> </td>
<td>A buildtool has to be used</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Decision</b> </td>
<td>Use Maven 3.0</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Arguments</b> </td>
<td>Maven provides an improved dependency management over Ant since dependencies are just defined in the POM and retrieved automatically. This considerably improves build stability and visibility since no dependencies need to be managed through the version control system. Also, since each dependency definition includes information about the dependency's version, builds become repeatable and more reliable. For new project members or other interested parties, the build is additionally further simplified to a few basic commands and a central file (the POM) which holds all important information.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Related decisions</b> </td>
<td> ANT</td>
</blockquote></tr>
</blockquote></tbody>
</table>