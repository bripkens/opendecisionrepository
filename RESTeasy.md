<table cellpadding='5' border='1' cellspacing='0' width='650'>
<blockquote><thead>
<blockquote><tr>
<blockquote><th width='150'> Name </th>
<th>RESTeasy</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td> <b>Problem/Issue</b> </td>
<td>An implementation of the JAX-RS specification needs to be chosen.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Decision</b> </td>
<td>RESTeasy allows interceptors which can be used for authentication purposes. This is necessary in order to use the existing ODR authentication credentials. RESTeasy will be used.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Arguments</b> </td>
<td>RESTeasy is a proven and well documented implementation of the JAX-RS implementation. It is also the default implementation of the JAX-RS specification for the JBoss application server. Unfortunately, EJB injections aren't working properly.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Related decisions</b> </td>
<td>
<ul>
<li><<caused by>> JAX-RS</li>
<li><code>&lt;&lt;replaces&gt;&gt;</code> Jersey</li>
</ul>
</td>
</blockquote></tr>
</blockquote></tbody>
</table>