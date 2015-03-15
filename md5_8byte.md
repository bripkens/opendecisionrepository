<table cellpadding='5' border='1' cellspacing='0' width='650'>
<blockquote><thead>
<blockquote><tr>
<blockquote><th width='150'> Name </th>
<th>md5 8byte salt 1000 interations for password encryption</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td> <b>Problem/Issue</b> </td>
<td>A secure way to save the password in the database has to be chosen</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Decision</b> </td>
<td>md5 with 8 Byte salt and 1000 iterations will be used.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Arguments</b> </td>
<td>
<ul>
<li>security</li>
<li>speed</li>
</ul>
</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Related decisions</b> </td>
<td>
<ul>
<li><code>&lt;&lt;replaces&gt;&gt;</code> sha-256 16 byte salt 1000 iterations</li>
</ul>
</td>
</blockquote></tr>
</blockquote></tbody>
</table>