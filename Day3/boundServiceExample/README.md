Bound services
===
A service is bound when an application component binds to it by calling bindService(). Bound service offers
a client-server interface that allows components to interact with the service, send requests and, get results. A bound service typically lives only while it serves another application component 
and does not run in the background indefinitely.
<br/><br/>
Components which can bind to Service are Activity, Service and Content Provider and we cannot use a
broadcast receiver to connect to a service.

Types of BoundService
---
* Local binding - the service and the component which wants to bind to that particular service are the
part of the same app process.
* Remote binding - the service and the component that wants to bind to
that particular service are in two different processes.

