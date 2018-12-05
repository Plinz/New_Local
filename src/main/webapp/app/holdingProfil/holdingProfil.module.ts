import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { NewLocalAdminModule } from 'app/admin/admin.module';
import {
    HoldingComponent,
    HoldingDetailComponent,
    HoldingUpdateComponent,
    HoldingDeletePopupComponent,
    HoldingDeleteDialogComponent,
    holdingRoute,
    holdingPopupRoute
} from './';

const ENTITY_STATES = [...holdingRoute, ...holdingPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HoldingComponent,
        HoldingDetailComponent,
        HoldingUpdateComponent,
        HoldingDeleteDialogComponent,
        HoldingDeletePopupComponent
    ],
    entryComponents: [HoldingComponent, HoldingUpdateComponent, HoldingDeleteDialogComponent, HoldingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalHoldingModule {}
