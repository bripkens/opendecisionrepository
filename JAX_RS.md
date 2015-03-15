<table cellpadding='5' border='1' cellspacing='0' width='650'>
<blockquote><thead>
<blockquote><tr>
<blockquote><th width='150'> Name </th>
<th>JAX-RS</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td> <b>Problem/Issue</b> </td>
<td>A stateless web service needs to be chosen.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Decision</b> </td>
<td>JAX-RS has been adopted widely and a lot of documentation exists. Setting up a web service requires few lines of code but still provides a fair amount of flexibility, e.g., changing HTTP headers. For this reasons JAX-RS will be used.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Arguments</b> </td>
<td>Since parts of the Java Enterprise Edition 6 are already used, it's useful to use more parts of it since they integrate well within the overall architecture. JAX-RS adds support for RESTful web services which fit very well into the overall target of a web service for a possible set of heterogeneous clients.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Related decisions</b> </td>
<td>
<ul>
<li><code>&lt;&lt;is caused by&gt;&gt;</code> Stateless web service</li>
<li><code>&lt;&lt;is caused by&gt;&gt;</code> Java Enterprise Edition 6</li>
</ul>
</td>
</blockquote></tr>
</blockquote></tbody>
</table>