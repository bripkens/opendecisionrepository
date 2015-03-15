**Table of Contents**



# Introduction #
This document adheres to [830-1998].

## Purpose ##
The document describes the requirements for the Open Decision Repository (hereinafter ODR). The ODR is a project of the Software Engineering and Architecture Group (SEARCH) at the University of Groningen (RUG). The originator is Mr. Uwe van Heesch. He is currently writing a paper about documentation and visualization of architectural design decisions together with Paris Avgeriou and Rich Hilliard. This project serves as a prototyp which may also be used to defend the paper.

Everybody who is interested in the intended functionality of the ODR may read this paper. This includes prospective developers and users.

## Scope ##
The Open Decision Repository will provide functionality to document and visualize decisions that have been made in a project. It's a freely available repository to store all decisions within a project. It provides functionallity to share the entire project with other members who can then get an insight into the decisions and the rationale behind them.
You can also get three different kinds of views.
  * The **chronical view** visualizes the chronological order in which decisions were made, i.e. the temporal aspect of the documentation.
  * A **stakeholder-involvement view** which can be generated for each documented iteration. It shows all new decisions and changes to existing ones and the persons that were involved in this decision making process.
  * The last one is the **relationship view** which visualizes the relationships among all the decisions.
Another very important point is the revision control which enables switching between differet versions of decisions. Thus the history and progress of the project can be reconstructed.

There are four main parts in the project:
  * project management (e.g. create a project)
  * user management (e.g. create user with different roles)
  * decision management (e.g. document a decision)
  * decision visualization (e.g. get a chronical visualization)
  * administration (administrate the ODR)


## Definitions, acronyms, and abbreviations ##
  * **ODR** Open Decision Repository
  * **SEARCH** Software Engineering and Architecture group
  * **RUG** University of Groningen


## References ##
  * **[830-1998]** http://standards.ieee.org/findstds/standard/830-1998.html
  * **[9126](9126.md)** http://www.iso.org/iso/catalogue_detail.htm?csnumber=22749
  * **[VfAD](VfAD.md)** Still to be published. Will later be found on http://www.cs.rug.nl/search/Publications/Publications

## Overview ##
The following section provides an overall description about the ODR, followed by specific requirements in section three and views in section four.




# Overall description #

## Stakeholders ##
Please read the [Project Plan](http://code.google.com/p/opendecisionrepository/wiki/ProjectPlan#Stakeholders) for more information about the stakeholders.

## Product perspective ##
The ODR aims to become a reference implementation of the viewpoints described in [VfAD](VfAD.md). In addition it should support the user by allowing him to
document software architecture in a simple and quick manner which as a result would allow him to spend more time on the actual design and implementation of the
business logic.

## Product functions ##
In section 1.2 (scope) four main functionality groups have been identified. The following figure shows them.
![http://opendecisionrepository.googlecode.com/svn/wiki/images/packages.png](http://opendecisionrepository.googlecode.com/svn/wiki/images/packages.png)

The requirements have been grouped according to this functionality groups.


## User characteristics ##
This project will be used by three different kinds of users.

  * Guest
The guest uses the system only to get a look into projects, which are pushlished. He can browse through the projects or find projects by using the search function.

  * User
The normal visitor of the system is a registrated user. He can create projects, can add members and manage the entire project. Project members have to take a role within a project like "software-architect", "manager" or "customer". Furthermore he is able to get a look in all views (chronical view, stakeholder-involvement view and relationship view) of his projects and other published projects.

  * Administrator
The administrator is able to manage the entities in the system. He can add common entities or delete unused entities.


## Constraints ##
The project is a non budget project therefore all technologies that will be used for the realization must be open source or at least free of charge.


## Assumptions and dependencies ##
None



# Specific requirements #
The requirements are documented in use case form. Some of these requirements contain the work flow for the creation or management of data. Italic text is used to visualize constraints, e.g. the maximum number of characters for an input field. When violated an error message is shown. It is possible to recover from such an error by undoing the previous action, e.g. clearing the input field.

| **ID** | **Name** |
|:-------|:---------|
| UC-1 | [register](http://code.google.com/p/opendecisionrepository/wiki/spuc_1) |
| UC-2 | [login](http://code.google.com/p/opendecisionrepository/wiki/spuc_2) |
| UC-3 | [create project](http://code.google.com/p/opendecisionrepository/wiki/spuc_3) |
| UC-4 | [edit project](http://code.google.com/p/opendecisionrepository/wiki/spuc_4) |
| UC-5 | [browse project](http://code.google.com/p/opendecisionrepository/wiki/spuc_5) |
| UC-6 | [delete project](http://code.google.com/p/opendecisionrepository/wiki/spuc_6) |
| UC-7 | [create iteration](http://code.google.com/p/opendecisionrepository/wiki/spuc_7) |
| UC-9 | [edit iteration](http://code.google.com/p/opendecisionrepository/wiki/spuc_9) |
| UC-10 | [delete iteration](http://code.google.com/p/opendecisionrepository/wiki/spuc_10) |
| UC-11 | [create decision](http://code.google.com/p/opendecisionrepository/wiki/spuc_11) |
| UC-12 | [browse decisions](http://code.google.com/p/opendecisionrepository/wiki/spuc_12) |
| UC-13 | [edit decision](http://code.google.com/p/opendecisionrepository/wiki/spuc_13) |
| UC-14 | [delete decision](http://code.google.com/p/opendecisionrepository/wiki/spuc_14) |
| UC-15 | [Retrieve RSS feed](http://code.google.com/p/opendecisionrepository/wiki/spuc_15) |
| UC-16 | [Quick add decision](http://code.google.com/p/opendecisionrepository/wiki/spuc_16) |
| UC-17 | [Relationship view](http://code.google.com/p/opendecisionrepository/wiki/spuc_17) |
| UC-19 | [Create concern](http://code.google.com/p/opendecisionrepository/wiki/spuc_19) |
| UC-20 | [Edit concern](http://code.google.com/p/opendecisionrepository/wiki/spuc_20) |
| UC-21 | [Browse concern](http://code.google.com/p/opendecisionrepository/wiki/spuc_21) |
| UC-22 | [Delete concern](http://code.google.com/p/opendecisionrepository/wiki/spuc_22) |
| UC-23 | [Chronological view](http://code.google.com/p/opendecisionrepository/wiki/spuc_17) |







# Non-functional requirements #

This section describes the non-functional requirements. Each of these non-functional requirements (NFR) has an id, as well as an assigned characteristic. These characteristics are the ones that are specified in [9126](9126.md).


<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-1</td>
<th width='80'>Characteristic</th>
<td>Functionality</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The ODR should provide a web interface. This web interface should at least support the following web browsers and newer versions of these browsers<br>
<ul>
<li>Microsoft Internet Explorer version 7</li>
<li>Mozilla Firefox version 3.5</li>
<li>Google Chrome version 5</li>
<li>Apple Safari version 5</li>
<li>Opera version 10</li>
</ul>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>


<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-2</td>
<th width='80'>Characteristic</th>
<td>Reliability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
Stored data must never get lost neither become inconsistent at any time.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-3</td>
<th width='80'>Characteristic</th>
<td>Usability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The web interface should not require any special expertise in the area of web applications. In addition it should support the user by providing <a href='http://ui-patterns.com/patterns/GoodDefaults'>good defaults</a>.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-4</td>
<th width='80'>Characteristic</th>
<td>Usability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The application should use the OPR corporate design. General layout should be the same also the way user interface components are arranged should not be changed. A new color scheme may be applied.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-5</td>
<th width='80'>Characteristic</th>
<td>Usability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
Help should be available for views that contain non-standard html components (like a calendar or drag and drop functionality).<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-6</td>
<th width='80'>Characteristic</th>
<td>Efficiency</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The maximal response time for a user action should be one second for an average application usage by 50 people.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-7</td>
<th width='80'>Characteristic</th>
<td>Efficiency</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
Generation of a visualization should take three seconds on average. Five seconds is the maximum that it is allowed to take. These numbers apply for an average application usage by 50 people.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-8</td>
<th width='80'>Characteristic</th>
<td>Maintainability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
It should be easy to extend the application with new visualizations, but also to change existing ones.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-9</td>
<th width='80'>Characteristic</th>
<td>Maintainability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
Create and update forms must be easily changeable because it is likely that they require changes.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-10</td>
<th width='80'>Characteristic</th>
<td>Maintainability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The application should be well tested (> 70% line coverage, > 60% path coverage). This includes units tests for POJOs and EJBs, as well as web interface tests using selenium.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-11</td>
<th width='80'>Characteristic</th>
<td>Portability</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
The application should be portable to any major OS, i.e. Linux distributions that are maximally two years old, Windows Server 2003 and newer, Windows XP and newer.<br>
</td>
</blockquote></tr>
</tbody>
</table></blockquote>

<table cellpadding='5' border='1' cellspacing='0' width='650'>
<thead>
<blockquote><tr>
<blockquote><th width='30'>ID</th>
<td width='50'>NFR-12</td>
<th width='80'>Characteristic</th>
<td>Security</td>
</blockquote></tr>
</thead>
<tbody>
<tr>
<blockquote><td>
Only encrypted passwords should be persisted. Passwords should be encrypted using at least MD5, an 8 byte salt and 1000 iterations.<br>
</td>
</blockquote></tr>
</tbody>
</table>