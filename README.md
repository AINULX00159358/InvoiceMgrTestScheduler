# InvoiceMgrTestScheduler

```
az container create -g MscResProj --name invoicetestscheduleraz --image docker.io/x00159358/invoicemgrtestscheduler:1.0.0-SNAPSHOT --cpu 2 --memory 4.0 --min-replicas 3 --min-replicas 5 --environment-variables  ENDPOINT=https://invoicemgr.northeurope-1.eventgrid.azure.net/api/events TPS=30 POST_TYPE=CLOUDEVENT
```
