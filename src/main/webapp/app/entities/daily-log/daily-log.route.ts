import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DailyLog } from 'app/shared/model/daily-log.model';
import { DailyLogService } from './daily-log.service';
import { DailyLogComponent } from './daily-log.component';
import { DailyLogDetailComponent } from './daily-log-detail.component';
import { DailyLogUpdateComponent } from './daily-log-update.component';
import { DailyLogDeletePopupComponent } from './daily-log-delete-dialog.component';
import { IDailyLog } from 'app/shared/model/daily-log.model';

@Injectable({ providedIn: 'root' })
export class DailyLogResolve implements Resolve<IDailyLog> {
  constructor(private service: DailyLogService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDailyLog> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((dailyLog: HttpResponse<DailyLog>) => dailyLog.body));
    }
    return of(new DailyLog());
  }
}

export const dailyLogRoute: Routes = [
  {
    path: '',
    component: DailyLogComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,desc',
      pageTitle: 'DailyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DailyLogDetailComponent,
    resolve: {
      dailyLog: DailyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DailyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DailyLogUpdateComponent,
    resolve: {
      dailyLog: DailyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DailyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DailyLogUpdateComponent,
    resolve: {
      dailyLog: DailyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DailyLogs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dailyLogPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DailyLogDeletePopupComponent,
    resolve: {
      dailyLog: DailyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DailyLogs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
