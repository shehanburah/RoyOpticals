import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { DailyLogComponent } from './daily-log.component';
import { DailyLogDetailComponent } from './daily-log-detail.component';
import { DailyLogUpdateComponent } from './daily-log-update.component';
import { DailyLogDeletePopupComponent, DailyLogDeleteDialogComponent } from './daily-log-delete-dialog.component';
import { dailyLogRoute, dailyLogPopupRoute } from './daily-log.route';

const ENTITY_STATES = [...dailyLogRoute, ...dailyLogPopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DailyLogComponent,
    DailyLogDetailComponent,
    DailyLogUpdateComponent,
    DailyLogDeleteDialogComponent,
    DailyLogDeletePopupComponent
  ],
  entryComponents: [DailyLogDeleteDialogComponent]
})
export class RoyOpticalsDailyLogModule {}
