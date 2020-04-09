import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { MonthlyLogComponent } from './monthly-log.component';
import { MonthlyLogDetailComponent } from './monthly-log-detail.component';
import { MonthlyLogUpdateComponent } from './monthly-log-update.component';
import { MonthlyLogDeletePopupComponent, MonthlyLogDeleteDialogComponent } from './monthly-log-delete-dialog.component';
import { monthlyLogRoute, monthlyLogPopupRoute } from './monthly-log.route';

const ENTITY_STATES = [...monthlyLogRoute, ...monthlyLogPopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MonthlyLogComponent,
    MonthlyLogDetailComponent,
    MonthlyLogUpdateComponent,
    MonthlyLogDeleteDialogComponent,
    MonthlyLogDeletePopupComponent
  ],
  entryComponents: [MonthlyLogDeleteDialogComponent]
})
export class RoyOpticalsMonthlyLogModule {}
