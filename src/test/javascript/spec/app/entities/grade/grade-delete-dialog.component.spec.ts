/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NewLocalTestModule } from '../../../test.module';
import { GradeDeleteDialogComponent } from 'app/entities/grade/grade-delete-dialog.component';
import { GradeService } from 'app/entities/grade/grade.service';

describe('Component Tests', () => {
    describe('Grade Management Delete Component', () => {
        let comp: GradeDeleteDialogComponent;
        let fixture: ComponentFixture<GradeDeleteDialogComponent>;
        let service: GradeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [GradeDeleteDialogComponent]
            })
                .overrideTemplate(GradeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GradeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GradeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
