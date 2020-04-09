import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MonthlyLog } from 'app/shared/model/monthly-log.model';
import { MonthlyLogService } from './monthly-log.service';
import { MonthlyLogComponent } from './monthly-log.component';
import { MonthlyLogDetailComponent } from './monthly-log-detail.component';
import { MonthlyLogUpdateComponent } from './monthly-log-update.component';
import { MonthlyLogDeletePopupComponent } from './monthly-log-delete-dialog.component';
import { IMonthlyLog } from 'app/shared/model/monthly-log.model';

@Injectable({ providedIn: 'root' })
export class MonthlyLogResolve implements Resolve<IMonthlyLog> {
  constructor(private service: MonthlyLogService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMonthlyLog> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((monthlyLog: HttpResponse<MonthlyLog>) => monthlyLog.body));
    }
    return of(new MonthlyLog());
  }
}

export const monthlyLogRoute: Routes = [
  {
    path: '',
    component: MonthlyLogComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'MonthlyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MonthlyLogDetailComponent,
    resolve: {
      monthlyLog: MonthlyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MonthlyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MonthlyLogUpdateComponent,
    resolve: {
      monthlyLog: MonthlyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MonthlyLogs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MonthlyLogUpdateComponent,
    resolve: {
      monthlyLog: MonthlyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MonthlyLogs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const monthlyLogPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MonthlyLogDeletePopupComponent,
    resolve: {
      monthlyLog: MonthlyLogResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'MonthlyLogs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
