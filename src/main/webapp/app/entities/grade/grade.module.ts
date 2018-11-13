import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { NewLocalAdminModule } from 'app/admin/admin.module';
import {
    GradeComponent,
    GradeDetailComponent,
    GradeUpdateComponent,
    GradeDeletePopupComponent,
    GradeDeleteDialogComponent,
    gradeRoute,
    gradePopupRoute
} from './';

const ENTITY_STATES = [...gradeRoute, ...gradePopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [GradeComponent, GradeDetailComponent, GradeUpdateComponent, GradeDeleteDialogComponent, GradeDeletePopupComponent],
    entryComponents: [GradeComponent, GradeUpdateComponent, GradeDeleteDialogComponent, GradeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalGradeModule {}
