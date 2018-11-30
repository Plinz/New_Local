import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from '../shared';
import { NewLocalAdminModule } from '../admin/admin.module';
import { ReviewRoute } from './review.route';
import { ReviewComponent } from './';
import { ChartsModule } from 'ng2-charts';
const ENTITY_STATES = [...ReviewRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES), ChartsModule],
    declarations: [ReviewComponent],
    entryComponents: [ReviewComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalReviewModule {}
