<table cellpadding='5' border='1' cellspacing='0' width='650'>
<blockquote><thead>
<blockquote><tr>
<blockquote><th width='150'> Name </th>
<th>Stateful web service</th>
</blockquote></tr>
</blockquote></thead>
<tbody>
<blockquote><tr>
<blockquote><td> <b>Problem/Issue</b> </td>
<td>A web service type needs to be selected.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Decision</b> </td>
<td>A stateful web service won't be used since scaling would become more complicated. In addition there are no benefits over a stateless web service.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Arguments</b> </td>
<td>A stateful web service in form of a SOAP based web service would allow simple communication between a client and the ODR. Also, state may be used to keep additional information about the users' sessions. A SOAP based web service is more complicated to consume, especially from JavaScript clients.</td>
</blockquote></tr>
<tr>
<blockquote><td> <b>Related decisions</b> </td>
<td>
<ul>
<li><code>&lt;&lt;is alternative for&gt;&gt;</code> Stateless web service</li>
</ul>
</td>
</blockquote></tr>
</blockquote></tbody>
</table>